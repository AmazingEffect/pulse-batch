package com.pulse.batch.repository.social;

import com.pulse.batch.entity.constant.MessageStatus;
import com.pulse.batch.entity.social.SocialGraphOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SocialGraphOutboxRepository extends JpaRepository<SocialGraphOutbox, Long> {

    List<SocialGraphOutbox> findByStatus(MessageStatus status);

}
