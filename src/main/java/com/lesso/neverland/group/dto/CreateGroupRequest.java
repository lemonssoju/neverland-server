package com.lesso.neverland.group.dto;

import java.util.List;

public record CreateGroupRequest(String name,
                                 List<Long> memberList) {}
