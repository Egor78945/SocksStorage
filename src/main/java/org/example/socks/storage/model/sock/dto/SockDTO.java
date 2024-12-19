package org.example.socks.storage.model.sock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SockDTO {
    private String color;
    private Integer cottonPercent;
    private Long count;
}
