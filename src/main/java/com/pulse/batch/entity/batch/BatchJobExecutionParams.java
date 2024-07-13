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
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "JOB_EXECUTION_ID", nullable = false)
    private BatchJobExecution jobExecution;

    @Column(name = "PARAMETER_NAME", nullable = false)
    private String parameterName;

    @Column(name = "PARAMETER_TYPE", nullable = false)
    private String parameterType;

    @Column(name = "PARAMETER_VALUE")
    private String parameterValue;

    @Column(name = "IDENTIFYING", nullable = false)
    private char identifying;

}