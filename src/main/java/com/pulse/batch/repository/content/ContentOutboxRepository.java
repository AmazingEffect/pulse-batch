package com.pulse.batch.repository.content;

import com.pulse.batch.entity.MessageStatus;
import com.pulse.batch.entity.content.ContentOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentOutboxRepository extends JpaRepository<ContentOutbox, Long> {

    List<ContentOutbox> findByStatus(MessageStatus status);

}
