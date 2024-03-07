package com.lesso.neverland.guestMemo.repository;

import com.lesso.neverland.guestMemo.domain.GuestMemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestMemoRepository extends JpaRepository<GuestMemo, Long> {
}
