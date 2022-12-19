package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tanhua.dubbo.mapper.QuestionsMapper;
import com.tanhua.model.domain.Question;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: 16420
 * @time: 2022/12/19 12:04
 */
@DubboService
public class QuestionsApiImpl implements QuestionsApi{

    @Autowired
    private QuestionsMapper questionsMapper;


    @Override
    public void updateById(Question question) {
        questionsMapper.updateById(question);
    }

    @Override
    public void saveQuestions(Question question) {
        questionsMapper.insert(question);
    }

    @Override
    public Question findByUserId(Long userId) {
        QueryWrapper<Question> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return questionsMapper.selectOne(qw);
    }
}
