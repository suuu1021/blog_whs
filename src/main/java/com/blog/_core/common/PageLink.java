package com.blog._core.common;

import lombok.Data;

@Data
public class PageLink {

    private int pageNumber; // 내부 페이지 번호
    private int displayNumber; // 사용자 표시 페이지 번호
    private boolean active; // 현재 페이지 여부

    public PageLink(int pageNumber, int displayNumber, boolean active) {
        this.pageNumber = pageNumber;
        this.displayNumber = displayNumber;
        this.active = active;
    }
}
