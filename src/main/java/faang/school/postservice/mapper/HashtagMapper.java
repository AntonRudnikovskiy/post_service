package faang.school.postservice.mapper;

import faang.school.postservice.dto.hashtag.HashtagDto;
import faang.school.postservice.model.Hashtag;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HashtagMapper {
    Hashtag dtoToEntity(HashtagDto hashtagDto);
    HashtagDto entityToDto(Hashtag hashtag);
    List<Hashtag> listDtoToEntity(List<HashtagDto> hashtagDto);
    List<HashtagDto> listEntityToDto(List<Hashtag> hashtag);
    Set<HashtagDto> setEntityToDto(Set<Hashtag> hashtag);
    Set<Hashtag> setDtoToEntity(Set<HashtagDto> hashtagDto);
}