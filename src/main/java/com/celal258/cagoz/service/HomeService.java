package com.celal258.cagoz.service;

import com.celal258.cagoz.entity.HomeTableView;
import com.celal258.cagoz.entity.Paper;
import com.celal258.cagoz.entity.Project;
import com.celal258.cagoz.repository.ProjectRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("HomeViewService")
@Transactional
public class HomeService {



    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private ProjectRepository projectRepository;

    public void getDbData(ObservableList<HomeTableView> tableList )
    {
        HomeTableView homeTableView;
        for(Project project:projectRepository.findAll()){
            homeTableView = new HomeTableView();
            Project project1=new Project();
           /* homeTableView.getProject().setId(project.getId());
            homeTableView.getProject().setProjectDescription(project.getProjectDescription());
            homeTableView.getProject().setProjectName(project.getProjectName());
            homeTableView.getProject().setProjectType(project.getProjectType());
            homeTableView.getProject().setProjectOrder(project.getProjectOrder());
            homeTableView.getProject().setProjectRemind(project.getProjectRemind());*/
            homeTableView.setProject(project);
            homeTableView.setCustomer(project.getCustomer());
            
            if(project.getPapers().size() != 0){
                Paper paper=project.getPapers().get(0);
                homeTableView.setPaper(paper);
                /*homeTableView.getPaper().setOperationType(paper.getOperationType());
                homeTableView.getPaper().setDateOfGoing(paper.getDateOfGoing());
                homeTableView.getPaper().setDateOfReturn(paper.getDateOfReturn());
                homeTableView.getPaper().setApprovalStatus(paper.getApprovalStatus());*/
            }
            else
                homeTableView.setPaper(new Paper());
            tableList.addAll(homeTableView);
        }
    }
}
