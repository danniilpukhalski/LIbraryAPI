
import com.modsen.booktrackerservice.domain.Tracker;
import com.modsen.booktrackerservice.domain.exception.DuplicateResourceException;
import com.modsen.booktrackerservice.domain.exception.ResourceNotFoundException;
import com.modsen.booktrackerservice.repository.TrackerRepository;
import com.modsen.booktrackerservice.service.impl.TrackerServiceImpl;
import com.modsen.booktrackerservice.dto.TrackerDto;
import com.modsen.booktrackerservice.dto.trackerStatusRequest;
import com.modsen.booktrackerservice.mapper.TrackMapper;
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
public class TrackerServiceImplTest {

    @Mock
    private TrackerRepository trackerRepository;

    @Mock
    private TrackMapper trackMapper;

    @InjectMocks
    private TrackerServiceImpl trackerService;

    @Test
    void testGetTrackerByBookId_TrackerExists() {
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
    void testGetTrackerByBookId_TrackerNotFound() {
        Long bookId = 1L;
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.getTrackerByBookId(bookId);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
    }

    @Test
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
    void testUpdateTrackerStatus_TrackerExists() {
        Long bookId = 1L;
        trackerStatusRequest request = new trackerStatusRequest();
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
    void testCreateTracker_TrackerAlreadyExists() {
        Long bookId = 1L;
        when(trackerRepository.findById(bookId)).thenReturn(Optional.of(new Tracker()));

        Exception exception = assertThrows(DuplicateResourceException.class, () -> {
            trackerService.createTracker(bookId);
        });
        assertEquals("Tracker already exists", exception.getMessage());
        verify(trackerRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetTrackerById_TrackerExists() {
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
    void testGetTrackerById_TrackerNotFound() {
        Long id = 1L;
        when(trackerRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.getTrackerById(id);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).findById(id);
    }
    @Test
    void testGetTrackersWhereStatusIsFree_StatusFreeExists() {
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
    void testGetTrackersWhereStatusIsFree_StatusFreeNotFound() {
        when(trackerRepository.findTrackerByStatus("free")).thenReturn(List.of());

        List<TrackerDto> result = trackerService.getTrackersWhereStatusIsFree();

        assertNotNull(result);
        assertEquals(0, result.size());
        verify(trackerRepository, times(1)).findTrackerByStatus("free");
        verify(trackMapper, times(1)).toTrackerDto(List.of());
    }

    @Test
    void testUpdateTracker_TrackerExists() {
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
    void testUpdateTracker_TrackerNotFound() {
        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setId(1L);
        when(trackerRepository.findById(trackerDto.getId())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.updateTracker(trackerDto);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).findById(trackerDto.getId());
        verify(trackerRepository, never()).save(any());
    }
    @Test
    void testSoftDeleteTrackerById_TrackerExists() {
        Long id = 1L;
        when(trackerRepository.existsById(id)).thenReturn(true);

        trackerService.softDeleteTrackerById(id);

        verify(trackerRepository, times(1)).existsById(id);
        verify(trackerRepository, times(1)).deleteById(id);
    }

    @Test
    void testSoftDeleteTrackerById_TrackerNotFound() {
        Long id = 1L;
        when(trackerRepository.existsById(id)).thenReturn(false);

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.softDeleteTrackerById(id);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).existsById(id);
        verify(trackerRepository, never()).deleteById(any());
    }
    @Test
    void testUpdateTrackerStatus_TrackerNotFound() {
        Long bookId = 1L;
        trackerStatusRequest request = new trackerStatusRequest();
        request.setStatus("taken");
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.updateTrackerStatus(bookId, request);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, never()).save(any());
        verify(trackMapper, never()).toTrackerDto(any(Tracker.class));
    }

    @Test
    void testUpdateTrackerStatus_ElseBranch() {
        Long bookId = 1L;
        trackerStatusRequest request = new trackerStatusRequest();
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
    void testSoftDeleteTrackerByBookId_TrackerNotFound() {
        Long bookId = 1L;
        when(trackerRepository.findTrackerByBookId(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            trackerService.softDeleteTrackerByBookId(bookId);
        });
        assertEquals("Tracker not found", exception.getMessage());
        verify(trackerRepository, times(1)).findTrackerByBookId(bookId);
        verify(trackerRepository, never()).save(any());
    }

}
