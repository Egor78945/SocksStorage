package org.example.socks.storage.service.sock;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.socks.storage.model.sock.dto.SockDTO;
import org.example.socks.storage.model.sock.entity.Sock;
import org.example.socks.storage.repository.sock.SockRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Tag(name = "Sock service", description = "Provides business logic with socks.")
public class SockService {
    private final SockRepository sockRepository;

    @Operation(description = "Registers new socks or updates existing.")
    public Long incomeSock(SockDTO sockDTO) {
        if (sockDTO.getColor() == null || sockDTO.getColor().length() < 3 || sockDTO.getColor().length() > 100) {
            throw new RuntimeException("Unknown color.");
        } else if (sockDTO.getCottonPercent() == null || sockDTO.getCottonPercent() < 0 || sockDTO.getCottonPercent() > 100) {
            throw new RuntimeException("Invalid cotton content. It must to be between 0 and 100.");
        } else if (sockDTO.getCount() == null || sockDTO.getCount() <= 0 || sockDTO.getCount() > 1_000_000) {
            throw new RuntimeException("Invalid sock count or it too much.");
        } else {
            Sock sock = sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent());
            if (sock == null) {
                return sockRepository.save(new Sock(sockDTO.getColor(), sockDTO.getCottonPercent(), sockDTO.getCount())).getCount();
            }
            sock.setCount(sock.getCount() + sockDTO.getCount());
            return sockRepository.save(sock).getCount();
        }
    }

    @Operation(description = "Takes socks from storage.")
    public Long outcomeSock(SockDTO sockDTO) {
        Sock sock = sockRepository.findSockByColorAndCottonPercent(sockDTO.getColor(), sockDTO.getCottonPercent());
        if (sock == null) {
            throw new RuntimeException("Socks with parameter like this is not found.");
        } else if (sockDTO.getCount() == null || sock.getCount() < sockDTO.getCount()) {
            throw new RuntimeException(String.format("Not enough socks in the storage. Actual count - %s, required count - %s", sock.getCount(), sockDTO.getCount()));
        } else {
            if (sock.getCount() - sockDTO.getCount() == 0) {
                sockRepository.delete(sock);
                return 0L;
            }
            sock.setCount(sock.getCount() - sockDTO.getCount());
            return sockRepository.save(sock).getCount();
        }

    }

    @Operation(description = "Returns the count of socks by constraints.")
    public Long countByConstraints(String color, Integer lessThan, Integer equal, Integer moreThan) {
        Optional<Long> count;
        if (!color.isEmpty()) {
            if (equal != -1) {
                count = sockRepository.findSockCountByConstraints(color, equal);
            } else if (lessThan == Integer.MAX_VALUE && moreThan == Integer.MIN_VALUE) {
                count = sockRepository.findSockCountByConstraint(color);
            } else {
                count = sockRepository.findSockCountByConstraints(color, lessThan, moreThan);
            }
        } else {
            if (equal != -1) {
                count = sockRepository.findSockCountByConstraint(equal);
            } else {
                count = sockRepository.findSockCountByConstraints(lessThan, moreThan);
            }
        }
        return count.orElseThrow(() -> new RuntimeException("Socks with constraints like this is not found."));
    }

    @Operation(description = "Update existing socks by details.")
    public void updateSocks(Long id, String color, Integer cottonPercent, Long count) {
        Sock sock = sockRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Socks with id %s is not found.", id)));
        if (count == 0) {
            sockRepository.delete(sock);
            return;
        }
        if (count > 0 && count <= 1_000_000) {
            sock.setCount(count);
        }
        if (color.length() >= 3 && color.length() <= 100) {
            sock.setColor(color);
        }
        if (cottonPercent >= 0 && cottonPercent <= 100) {
            sock.setCottonPercent(cottonPercent);
        }
        sockRepository.save(sock);
    }
}
