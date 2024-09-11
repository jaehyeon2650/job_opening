package project.general_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.general_project.domain.Assessment;
import project.general_project.domain.Member;
import project.general_project.repository.assessment.AssessmentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssessmentService {
    private final AssessmentRepository repository;

    @Transactional
    public void createAssessment(Member toMember,Member fromMember,String content,int score){
        Assessment assessment=new Assessment(toMember,fromMember,score,content);
        repository.save(assessment);
    }

    public List<Assessment> getAllAssessment(Long memberId){
        return repository.getAllAssessment(memberId);
    }

    public Long getCount(Long memberId){
        return repository.getAssessmentCount(memberId);
    }

    public double getAverage(Long memberId){
        return repository.getAverageScore(memberId);
    }
}
