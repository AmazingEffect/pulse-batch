package com.pulse.batch.repository.member;

import com.pulse.batch.entity.constant.MessageStatus;
import com.pulse.batch.entity.member.MemberOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberOutboxRepository extends JpaRepository<MemberOutbox, Long> {

    List<MemberOutbox> findByStatus(MessageStatus status);

}
