package com.example.presensi_app.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PageResponse<T> {
    private int page;
    private int size;
    private long total;
    private List<T> dataPegawai;

    public static <T> PageResponse<T> success(List<T> dataPegawai, int page, int size, long total) {
        return PageResponse.<T>builder()
                .page(page)
                .size(size)
                .total(total)
                .dataPegawai(dataPegawai)
                .build();
    }
}
