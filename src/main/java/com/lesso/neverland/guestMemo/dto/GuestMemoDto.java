package com.lesso.neverland.guestMemo.dto;

import java.time.LocalDate;

public record GuestMemoDto(String writer,
                           String profileImage,
                           String content,
                           LocalDate createdDate) {}
