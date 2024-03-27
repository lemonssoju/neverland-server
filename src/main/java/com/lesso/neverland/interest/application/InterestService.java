package com.lesso.neverland.interest.application;

import com.lesso.neverland.common.enums.Contents;
import com.lesso.neverland.interest.domain.Interest;
import com.lesso.neverland.interest.repository.InterestRepository;
import com.lesso.neverland.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    public void createInterest(Contents contents, Contents preference, User user) {
        Interest interest = Interest.builder()
                .contents(contents)
                .preference(preference)
                .user(user)
                .build();
        interest.setUser(user);
        interestRepository.save(interest);
    }
}
