package com.lesso.neverland.group.dto;

import java.util.List;

public record EditGroupRequest(String name,
                               String subName,
                               List<Long> memberList) {}
