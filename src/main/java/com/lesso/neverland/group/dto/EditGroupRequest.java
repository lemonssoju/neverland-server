package com.lesso.neverland.group.dto;

import java.util.List;

public record EditGroupRequest(String name,
                               String startDate,
                               List<Long> memberList) {}
