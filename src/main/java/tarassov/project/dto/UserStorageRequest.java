package tarassov.project.dto;

import lombok.Data;

@Data
public class UserStorageRequest {
    private Long userId;
    private Long storageId;
}