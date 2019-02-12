package com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Memo {
    private Long id;
    private String content;
    private Long time;
}

