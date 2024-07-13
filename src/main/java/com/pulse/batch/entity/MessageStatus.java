package com.pulse.batch.entity;

public enum MessageStatus {

    PENDING,   // 대기
    PROCESSED, // 처리완료
    SUCCESS,   // 성공
    FAIL,      // 실패
    REISSUE    // 재발행

}
