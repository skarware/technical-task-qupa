package dev.skaringa.qupa.provider

import dev.skaringa.qupa.api.nasdaq.dto.Response

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime

class NasdaqDatasetDtoProvider {
    private static final def ID = 1
    private static final def DATASET_CODE = "datasetCode"
    private static final def REFRESHED_AT = ZonedDateTime.of(LocalDateTime.of(2022, 9, 4, 17, 18, 19, 123456789), ZoneOffset.UTC)
    private static final def NEWEST_AVAILABLE_DATE = LocalDate.of(2022, 9, 4)
    private static final def OLDEST_AVAILABLE_DATE = LocalDate.of(2010, 9, 4)
    private static final def START_DATE = LocalDate.of(2010, 1, 1)
    private static final def END_DATE = LocalDate.of(2010, 1, 5)
    private static final Map SAMPLE = [
            id                 : ID,
            datasetCode        : DATASET_CODE,
            refreshedAt        : REFRESHED_AT,
            newestAvailableDate: NEWEST_AVAILABLE_DATE,
            oldestAvailableDate: OLDEST_AVAILABLE_DATE,
            startDate          : START_DATE,
            endDate            : END_DATE]

    static def dto(Map<String, Object> map = [:]) {
        def props = SAMPLE + [data: [NasdaqDatasetDataEntryDtoProvider.dto()]] + map
        return new Response.Dataset(
                props.id as Integer,
                props.datasetCode as String,
                props.refreshedAt as ZonedDateTime,
                props.newestAvailableDate as LocalDate,
                props.oldestAvailableDate as LocalDate,
                props.startDate as LocalDate,
                props.endDate as LocalDate,
                props.data as List<Response.Dataset.DataEntry>)
    }
}
