package rest_with_spring_boot_and_java.data.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UploadFileResponseDTO implements Serializable {

    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}
