package com.pulse.batch.entity.batch;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
public class BatchStepExecutionContext {

    @Id
    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;

    @OneToOne
    @JoinColumn(name = "STEP_EXECUTION_ID", referencedColumnName = "STEP_EXECUTION_ID", insertable = false, updatable = false)
    private BatchStepExecution stepExecution;

    @Column(name = "SHORT_CONTEXT", nullable = false)
    private String shortContext;

    @Column(name = "SERIALIZED_CONTEXT")
    private String serializedContext;

}