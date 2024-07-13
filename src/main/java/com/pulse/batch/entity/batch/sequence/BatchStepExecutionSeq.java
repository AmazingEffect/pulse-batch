package com.pulse.batch.entity.batch.sequence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "BATCH_STEP_EXECUTION_SEQ")
public class BatchStepExecutionSeq {

    @Id
    @Column(name = "ID")
    private Long id;

}