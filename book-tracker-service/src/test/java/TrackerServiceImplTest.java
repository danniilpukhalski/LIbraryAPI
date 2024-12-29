import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.domain.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.TrackerStatusRequest;
import com.modsen.booktrackerservice.mapper.TrackMapper;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.impl.TrackerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TrackerService tests")
public class TrackerServiceImplTest {

    @Mock
    private TrackerRepository trackerRepository;

    @Mock
    private TrackMapper trackMapper;

    @InjectMocks
    private TrackerServiceImpl trackerService;

    @Test
    @DisplayName("testGetTrackerByBookIdTrackerExists")
    void testGetTrackerByBookIdTrackerExists() {
        Long bookId = 1L;
        Tracker tracker = new Tracker();
        TrackerDto trackerDto = new TrackerDto();
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.of(tracker));
        when(trackMapper.toTrackerDto(tracker)).thenReturn(trackerDto);

        TrackerDto result = trackerService.getTrackerByBookId(bookId);

        assertNotNull(result);
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackMapper, times(1)).toTrackerDto(tracker);
    }

    @Test
    @DisplayName("testGetTrackerByBookIdTrackerNotFound")
    void testGetTrackerByBookIdTrackerNotFound() {
        Long bookId = 1L;
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.getTrackerByBookId(bookId);
        });
        assertEquals("Tracker with bookId 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
    }

    @Test
    @DisplayName("testGetAllTrackers")
    void testGetAllTrackers() {
        List<Tracker> trackers = Arrays.asList(new Tracker(), new Tracker());
        List<TrackerDto> trackerDtos = Arrays.asList(new TrackerDto(), new TrackerDto());
        when(trackerRepository.findAll()).thenReturn(trackers);
        when(trackMapper.toTrackerDto(trackers)).thenReturn(trackerDtos);

        List<TrackerDto> result = trackerService.getAllTrackers();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trackerRepository, times(1)).findAll();
        verify(trackMapper, times(1)).toTrackerDto(trackers);
    }

    @Test
    @DisplayName("testUpdateTrackerStatusTrackerExists")
    void testUpdateTrackerStatusTrackerExists() {
        Long bookId = 1L;
        TrackerStatusRequest request = new TrackerStatusRequest();
        request.setStatus("taken");
        Tracker tracker = new Tracker();
        TrackerDto trackerDto = new TrackerDto();
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.of(tracker));
        when(trackMapper.toTrackerDto(tracker)).thenReturn(trackerDto);

        TrackerDto result = trackerService.updateTrackerStatus(bookId, request);

        assertNotNull(result);
        assertEquals("taken", tracker.getStatus());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, times(1)).save(tracker);
        verify(trackMapper, times(1)).toTrackerDto(tracker);
    }

    @Test
    @DisplayName("testCreateTrackerTrackerAlreadyExists")
    void testCreateTrackerTrackerAlreadyExists() {
        Long bookId = 1L;
        when(trackerRepository.findById(bookId)).thenReturn(Optional.of(new Tracker()));

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            trackerService.createTracker(bookId);
        });
        assertEquals("Tracker with bookId 1 already exists", exception.getMessage());
        verify(trackerRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("testGetTrackerByIdTrackerExists")
    void testGetTrackerByIdTrackerExists() {
        Long id = 1L;
        Tracker tracker = new Tracker();
        TrackerDto trackerDto = new TrackerDto();
        when(trackerRepository.findById(id)).thenReturn(Optional.of(tracker));
        when(trackMapper.toTrackerDto(tracker)).thenReturn(trackerDto);

        TrackerDto result = trackerService.getTrackerById(id);

        assertNotNull(result);
        verify(trackerRepository, times(1)).findById(id);
        verify(trackMapper, times(1)).toTrackerDto(tracker);
    }

    @Test
    @DisplayName("testGetTrackerByIdTrackerNotFound")
    void testGetTrackerByIdTrackerNotFound() {
        Long id = 1L;
        when(trackerRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.getTrackerById(id);
        });
        assertEquals("Tracker with id 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).findById(id);
    }
    @Test
    @DisplayName("testGetTrackersWhereStatusIsFreeStatusFreeExists")
    void testGetTrackersWhereStatusIsFreeStatusFreeExists() {
        List<Tracker> trackers = Arrays.asList(new Tracker(), new Tracker());
        List<TrackerDto> trackerDtos = Arrays.asList(new TrackerDto(), new TrackerDto());
        when(trackerRepository.findTrackerByStatus("free")).thenReturn(trackers);
        when(trackMapper.toTrackerDto(trackers)).thenReturn(trackerDtos);

        List<TrackerDto> result = trackerService.getTrackersWhereStatusIsFree();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(trackerRepository, times(1)).findTrackerByStatus("free");
        verify(trackMapper, times(1)).toTrackerDto(trackers);
    }

    @Test
    @DisplayName("testGetTrackersWhereStatusIsFreeStatusFreeNotFound")
    void testGetTrackersWhereStatusIsFreeStatusFreeNotFound() {
        when(trackerRepository.findTrackerByStatus("free")).thenReturn(List.of());

        List<TrackerDto> result = trackerService.getTrackersWhereStatusIsFree();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(trackerRepository, times(1)).findTrackerByStatus("free");
        verify(trackMapper, times(1)).toTrackerDto(List.of());
    }

    @Test
    @DisplayName("testUpdateTrackerTrackerExists")
    void testUpdateTrackerTrackerExists() {
        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setId(1L);
        Tracker tracker = new Tracker();
        when(trackerRepository.findById(trackerDto.getId())).thenReturn(Optional.of(tracker));
        when(trackMapper.toEntity(trackerDto)).thenReturn(tracker);

        TrackerDto result = trackerService.updateTracker(trackerDto);

        assertNotNull(result);
        assertEquals(trackerDto, result);
        verify(trackerRepository, times(1)).findById(trackerDto.getId());
        verify(trackerRepository, times(1)).save(tracker);
    }

    @Test
    @DisplayName("testUpdateTrackerTrackerNotFound")
    void testUpdateTrackerTrackerNotFound() {
        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setId(1L);
        when(trackerRepository.findById(trackerDto.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.updateTracker(trackerDto);
        });
        assertEquals("Tracker with id 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).findById(trackerDto.getId());
        verify(trackerRepository, never()).save(any());
    }
    @Test
    @DisplayName("testDeleteTrackerByIdTrackerExists")
    void testDeleteTrackerByIdTrackerExists() {
        Long id = 1L;
        when(trackerRepository.existsById(id)).thenReturn(true);

        trackerService.deleteTrackerById(id);

        verify(trackerRepository, times(1)).existsById(id);
        verify(trackerRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("testDeleteTrackerByIdTrackerNotFound")
    void testDeleteTrackerByIdTrackerNotFound() {
        Long id = 1L;
        when(trackerRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.deleteTrackerById(id);
        });
        assertEquals("Tracker with id 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).existsById(id);
        verify(trackerRepository, never()).deleteById(any());
    }
    @Test
    @DisplayName("testUpdateTrackerStatusTrackerNotFound")
    void testUpdateTrackerStatusTrackerNotFound() {
        Long bookId = 1L;
        TrackerStatusRequest request = new TrackerStatusRequest();
        request.setStatus("taken");
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.updateTrackerStatus(bookId, request);
        });
        assertEquals("Tracker with bookId 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, never()).save(any());
        verify(trackMapper, never()).toTrackerDto(any(Tracker.class));
    }

    @Test
    @DisplayName("testUpdateTrackerStatusElseBranch")
    void testUpdateTrackerStatusElseBranch() {
        Long bookId = 1L;
        TrackerStatusRequest request = new TrackerStatusRequest();
        request.setStatus("returned");
        Tracker tracker = new Tracker();
        TrackerDto trackerDto = new TrackerDto();
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.of(tracker));
        when(trackMapper.toTrackerDto(tracker)).thenReturn(trackerDto);

        TrackerDto result = trackerService.updateTrackerStatus(bookId, request);

        assertNotNull(result);
        assertEquals("returned", tracker.getStatus());
        assertNotNull(tracker.getReturned()); // Ensures the returned date is set
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, times(1)).save(tracker);
        verify(trackMapper, times(1)).toTrackerDto(tracker);
    }

    @Test
    @DisplayName("testDeleteTrackerByBookIdTrackerNotFound")
    void testDeleteTrackerByBookIdTrackerNotFound() {
        Long bookId = 1L;
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.deleteTrackerByBookId(bookId);
        });
        assertEquals("Tracker with bookId 1 not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, never()).save(any());
    }

}
