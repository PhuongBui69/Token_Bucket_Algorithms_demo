package com.example.demo.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TriangleRequest {
    @NotNull(message = "HEIGHT_NULL")
    double height;

    @NotNull(message = "WIDTH_NULL")
    double width;

}