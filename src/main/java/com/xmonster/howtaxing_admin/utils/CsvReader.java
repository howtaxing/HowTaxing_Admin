package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.dto.house.HousePubLandPriceInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class CsvReader {
    @Bean
    public ItemReader<? extends HousePubLandPriceInfoDto> csvFileItemReader() {
        /* file read */
        FlatFileItemReader<HousePubLandPriceInfoDto> flatFileItemReader = new FlatFileItemReader<>();
        flatFileItemReader.setResource(new FileSystemResource("/files/csv/PubLandPriceInfo_2023.csv"));
        flatFileItemReader.setLinesToSkip(1); // header line skip
        //flatFileItemReader.setEncoding("UTF-8"); // encoding
        flatFileItemReader.setEncoding("EUC-KR"); // encoding

        /* read하는 데이터를 내부적으로 LineMapper을 통해 Mapping */
        DefaultLineMapper<HousePubLandPriceInfoDto> defaultLineMapper = new DefaultLineMapper<>();

        /* delimitedLineTokenizer : setNames를 통해 각각의 데이터의 이름 설정 */
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames(
                "baseYear", "baseMonth", "legalDstCode", "roadAddr", "siDo", "siGunGu", "eupMyun",
                "dongRi", "specialLandCode", "bonNo", "bueNo", "specialLandName", "complexName",
                "dongName", "hoName", "area", "pubLandPrice", "complexCode", "dongCode", "hoCode");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        /* beanWrapperFieldSetMapper : Tokenizer에서 가지고온 데이터들을 VO로 바인드하는 역할 */
        BeanWrapperFieldSetMapper<HousePubLandPriceInfoDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(HousePubLandPriceInfoDto.class);

        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        /* lineMapper 지정 */
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }
}