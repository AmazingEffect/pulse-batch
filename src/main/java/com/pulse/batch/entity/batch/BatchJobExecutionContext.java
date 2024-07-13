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
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
public class BatchJobExecutionContext {

    @Id
    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

    @OneToOne
    @JoinColumn(name = "JOB_EXECUTION_ID", referencedColumnName = "JOB_EXECUTION_ID", insertable = false, updatable = false)
    private BatchJobExecution jobExecution;

    @Column(name = "SHORT_CONTEXT", nullable = false)
    private String shortContext;

    @Column(name = "SERIALIZED_CONTEXT")
    private String serializedContext;

}