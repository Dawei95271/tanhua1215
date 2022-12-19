package com.tanhua.dubbo.api;

import com.tanhua.model.domain.Question;

public interface QuestionsApi {

    // 根绝userId查询
    Question findByUserId(Long userId);

    // 保存questions
    void saveQuestions(Question question);

    // 根据Id更新
    void updateById(Question question);
}
