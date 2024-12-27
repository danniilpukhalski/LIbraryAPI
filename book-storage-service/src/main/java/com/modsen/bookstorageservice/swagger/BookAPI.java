package com.modsen.bookstorageservice.swagger;

import com.modsen.bookstorageservice.domain.exception.ExceptionBody;
import com.modsen.bookstorageservice.dto.BookDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Tag(name = "Book Controller", description = "Book API")
public interface BookAPI {

    @Operation(summary = "Create book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Book created successfully", content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    BookDto createBook(@RequestBody @Valid BookDto createBookRequest);

    @Operation(summary = "Update book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully", content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    BookDto updateBook(@PathVariable Long id, @RequestBody @Valid BookDto updateBookRequest);

    @Operation(summary = "Delete book")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    void deleteBook(@PathVariable Long id);

    @Operation(summary = "Get book by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully", content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    BookDto getBookById(@PathVariable Long id);

    @Operation(summary = "Get all books")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully", content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    List<BookDto> getAllBooks();

    @Operation(summary = "Get book by ISBN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book retrieved successfully", content = @Content(schema = @Schema(implementation = BookDto.class))),
            @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = ExceptionBody.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ExceptionBody.class)))
    })
    BookDto getBookByIsbn(@PathVariable String isbn);


}