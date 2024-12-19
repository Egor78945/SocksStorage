package org.example.socks.storage.service.sock;

import net.bytebuddy.dynamic.DynamicType;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.example.socks.storage.model.sock.entity.Sock;
import org.example.socks.storage.repository.sock.SockRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SockServiceTest {
    @InjectMocks
    SockService sockService;
    @Mock
    SockRepository sockRepository;

    @Test
    public void incomeSockInvalidColorTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("");
        Assertions.assertThrows(RuntimeException.class, () -> sockService.incomeSock(sockDTO));
    }

    @Test
    public void incomeSockInvalidCottonPercentTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(-1);
        Assertions.assertThrows(RuntimeException.class, () -> sockService.incomeSock(sockDTO));
    }

    @Test
    public void incomeSockInvalidCountTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_001L);
        Assertions.assertThrows(RuntimeException.class, () -> sockService.incomeSock(sockDTO));
    }

    @Test
    public void successfullyIncomeExistsTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Sock sock = new Sock(1L, sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount());
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(sock);
        Mockito.when(sockRepository.save(sock)).thenReturn(sock);
        Assertions.assertEquals(sockService.incomeSock(sockDTO), 2_000_000L);
    }

    @Test
    public void successfullyNotIncomeExistsTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Sock sock = new Sock(sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount());
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(null);
        Mockito.when(sockRepository.save(sock)).thenReturn(sock);
        Assertions.assertEquals(sockService.incomeSock(sockDTO), 1_000_000L);
    }

    @Test
    public void outcomeBySocksIsNotFoundTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(null);
        Assertions.assertThrows(RuntimeException.class, () -> sockService.outcomeSock(sockDTO));
    }

    @Test
    public void outcomeByCountLessThanConstraintsTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Sock sock = new Sock(1L, sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount()-5000);
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(sock);
        Assertions.assertThrows(RuntimeException.class, () -> sockService.outcomeSock(sockDTO));
    }

    @Test
    public void outcomeByCountEqualConstraintsTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Sock sock = new Sock(1L, sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount());
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(sock);
        Mockito.doNothing().when(sockRepository).delete(sock);
        Assertions.assertEquals(sockService.outcomeSock(sockDTO), 0L);
    }

    @Test
    public void successfullyOutcomeByCountConstraintsTest(){
        SockDTO sockDTO = new SockDTO();
        sockDTO.setColor("red");
        sockDTO.setCottonPercent(10);
        sockDTO.setCount(1_000_000L);
        Sock sock = new Sock(1L, sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount()+5000);
        Mockito.when(sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent())).thenReturn(sock);
        Mockito.doReturn(sock).when(sockRepository).save(sock);
        Assertions.assertEquals(sockService.outcomeSock(sockDTO), sock.getCount());
    }

    @Test
    public void countByColorAndEqualTest(){
        Optional<Long> opt = Optional.of(1L);
        Mockito.when(sockRepository.findSockCountByConstraints("abc", 1)).thenReturn((opt));
        Assertions.assertEquals(sockService.countByConstraints("abc", Integer.MAX_VALUE, 1, Integer.MIN_VALUE), 1L);
    }

    @Test
    public void countByColorTest(){
        Optional<Long> opt = Optional.of(1L);
        Mockito.when(sockRepository.findSockCountByConstraint("abc")).thenReturn((opt));
        Assertions.assertEquals(sockService.countByConstraints("abc", Integer.MAX_VALUE, -1, Integer.MIN_VALUE), 1L);
    }

    @Test
    public void countByColorAndLessThanTest(){
        Optional<Long> opt = Optional.of(1L);
        Mockito.when(sockRepository.findSockCountByConstraints("abc", Integer.MAX_VALUE, 3)).thenReturn((opt));
        Assertions.assertEquals(sockService.countByConstraints("abc", Integer.MAX_VALUE, -1, 3), 1L);
    }

    @Test
    public void countByEqualTest(){
        Optional<Long> opt = Optional.of(1L);
        Mockito.when(sockRepository.findSockCountByConstraint(1)).thenReturn((opt));
        Assertions.assertEquals(sockService.countByConstraints("", Integer.MAX_VALUE, 1, Integer.MIN_VALUE), 1L);
    }

    @Test
    public void countByLessThanOrMoreThanTest(){
        Optional<Long> opt = Optional.of(1L);
        Mockito.when(sockRepository.findSockCountByConstraints(5, Integer.MIN_VALUE)).thenReturn((opt));
        Assertions.assertEquals(sockService.countByConstraints("", 5, -1, Integer.MIN_VALUE), 1L);
    }


}
