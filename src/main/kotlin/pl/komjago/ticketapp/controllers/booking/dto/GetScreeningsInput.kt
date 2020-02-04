package pl.komjago.ticketapp.controllers.booking.dto

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDateTime

data class GetScreeningsInput(
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @ApiModelProperty(required = true, example = "2016-11-16 06:43")
        val from: LocalDateTime,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        @ApiModelProperty(required = true, example = "2020-11-16 06:43")
        val to: LocalDateTime
)