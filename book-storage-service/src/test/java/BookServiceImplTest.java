import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.modsen.bookstorageservice.domain.Book;
import com.modsen.bookstorageservice.domain.exception.DuplicateResourceException;
import com.modsen.bookstorageservice.domain.exception.ResourceNotFoundException;
import com.modsen.bookstorageservice.repository.BookRepository;
import com.modsen.bookstorageservice.service.impl.BookServiceImpl;
import com.modsen.bookstorageservice.web.dto.BookDto;
import com.modsen.bookstorageservice.web.mapper.BookMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void testGetBookById_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookById(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookById_NotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookById(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testCreateBook_Success() {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("123456789");

        Book book = new Book();
        book.setIsbn("123456789");

        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.createBook(bookDto);

        assertNotNull(result);
        assertEquals("123456789", result.getIsbn());
        verify(bookRepository, times(1)).findByIsbn("123456789");
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testCreateBook_Duplicate() {
        BookDto bookDto = new BookDto();
        bookDto.setIsbn("123456789");

        Book book = new Book();
        book.setIsbn("123456789");

        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.findByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        assertThrows(DuplicateResourceException.class, () -> bookService.createBook(bookDto));
        verify(bookRepository, times(1)).findByIsbn("123456789");
    }

    @Test
    void testUpdateBook_Success() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);

        Book book = new Book();
        book.setId(1L);

        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        BookDto result = bookService.updateBook(bookDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook_NotFound() {
        BookDto bookDto = new BookDto();
        bookDto.setId(1L);

        Book book = new Book();
        book.setId(1L);

        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.updateBook(bookDto));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testSoftDeleteBook_Success() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        bookService.softDeleteBook(bookId);

        assertTrue(book.getDeleted());
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testSoftDeleteBook_NotFound() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.softDeleteBook(bookId));
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookByIsbn_Success() {
        String isbn = "1234567890";
        Book book = new Book();
        book.setId(1L);
        book.setIsbn(isbn);

        BookDto bookDto = new BookDto();
        bookDto.setId(1L);
        bookDto.setIsbn(isbn);

        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto result = bookService.getBookByIsbn(isbn);

        assertNotNull(result);
        assertEquals(isbn, result.getIsbn());
        verify(bookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void testGetBookByIsbn_NotFound() {
        String isbn = "1234567890";
        when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.getBookByIsbn(isbn));
        verify(bookRepository, times(1)).findByIsbn(isbn);
    }

    @Test
    void testGetAllBooksWhenBooksExist() {
        List<Book> books = List.of(
                new Book(1L, "978-3-16-1485678410-0", "A Clockwork Orange", "Fantasy", "A thought-provoking novel about free will and morality.",
                        "Anthony Burgess", false),
                new Book(2L,"978-0-7432-7356-5", "The Great Gatsby", "Classic", "A novel about the American dream and the jazz age.",
                        "F. Scott Fitzgerald", false)
        );
        List<BookDto> bookDtos = List.of(
                new BookDto(1L, "978-3-16-1485678410-0", "A Clockwork Orange", "Fantasy", "A thought-provoking novel about free will and morality.",
                        "Anthony Burgess"),
                new BookDto(2L,"978-0-7432-7356-5", "The Great Gatsby", "Classic", "A novel about the American dream and the jazz age.",
                        "F. Scott Fitzgerald")
        );
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDto(books)).thenReturn(bookDtos);

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("978-3-16-1485678410-0", result.get(0).getIsbn());
        assertEquals("978-0-7432-7356-5", result.get(1).getIsbn());
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDto(books);
    }


    @Test
    void testGetAllBooksWhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());
        when(bookMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(bookRepository, times(1)).findAll();
        verify(bookMapper, times(1)).toDto(Collections.emptyList());
    }
}
