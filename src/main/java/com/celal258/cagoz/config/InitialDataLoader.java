package com.celal258.cagoz.config;


import com.celal258.cagoz.entity.*;
import com.celal258.cagoz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;


@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = true;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private RemindRepository remindRepository;

    @Autowired
    private LoggingEventTypeRepository loggingEventTypeRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        /*if (alreadySetup)
            return;*/
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);

        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));
        User user = createUserIfNotFound("mos","ROLE_ADMIN");
        User user2 = createUserIfNotFound("ycl","ROLE_ADMIN");
        User user3 = createUserIfNotFound("cll","ROLE_USER");

        String LoggingTypesArray[] = {"adlı bir müşteri eklendi" , "adlı müşterinin bilgileri güncellendi","adlı müşteri silindi","isimli proje eklendi",
        "isimli işlem eklendi" ,"adında bir döküman eklendi","isimli projeye hatırlatma eklendi" ,"isimli projeye ait hatırlatma silindi",
        "isimli projenin bilgileri düzenlendi","adında bir döküman silindi","isimli proje silindi","adlı müşteri silinirken silindi","işlemine bir hatırlatma eklendi",
        "isimli işlemi düzenlendi","işleminin hatırlatması silindi"};

        createLoggingTypesIfNotFound(LoggingTypesArray);
        /*
        Customer c1 = new Customer();
        c1.setFirstName("Mahmut");
        c1.setLastName("Ekrem");
        c1.setCompany("Mamula");
        //c1.setDebt("asddsa");
        c1.setNumber("053785545");
        c1.setUser(u);
        customerRepository.save(c1);

        c1 = new Customer();
        c1.setFirstName("Arslan");
        c1.setLastName("Simsek");
        c1.setCompany("Asimsek");
        //c1.setDebt("qwez xcsf");
        c1.setNumber("053785545");
        c1.setUser(u);
        customerRepository.save(c1);
        for(Customer customer: customerRepository.findAll()){
            c1=customer;
        }



        Project p1=new Project();
        p1.setCustomer(c1);
        p1.setDate(new Date());
        p1.setProjectType("Yeni Abone");
        p1.setProjectName("Proje Adı 1");
        p1.setPrice(12000);
        p1.setProjectDescription("");
        p1.setReceivedMoney(200);
        p1.setProjectOrder(p1.getPrice()-p1.getReceivedMoney() == 0 ? "Ödendi" : "Ödenmedi");
        p1.setProjectRemind("Var");
        p1.setProjectDescription("proje açıklaması");
        p1.setUser(u);
        projectRepository.save(p1);
        for(Project project: projectRepository.findAll()){
            p1=project;
        }

        Remind r1 = new Remind();
        try {
            r1.setStartDate(new SimpleDateFormat("yyyy-MM-dd").parse("2019-06-09"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        r1.setProject(p1);
        r1.setDescription("Hatirlatma için açıklama");
        r1.setRepeat("5");
        remindRepository.save(r1);
        for(Remind remind: remindRepository.findAll()){
            r1=remind;
        }

        Document d1=new Document();
        d1.setProject(p1);
        d1.setProjectDraw(PRESENT);
        d1.setPathProjectDraw("");
        documentRepository.save(d1);

        Paper pa1=new Paper();
        pa1.setProject(p1);
        pa1.setOperationType("Enerji Müsadesi");
        pa1.setDateOfGoing(new Date());
        pa1.setDateOfReturn(new Date());
        pa1.setApprovalStatus("Onaylandı");
        pa1.setPaperNote("Mertin alacagı iş");
        paperRepository.save(pa1);
        pa1=new Paper();
        pa1.setProject(p1);
        pa1.setOperationType("Yapı Denetim");
        pa1.setDateOfGoing(new Date());
        pa1.setApprovalStatus("Onaylanmadı");

        try {
            pa1.setDateOfReturn( new SimpleDateFormat("yyyy-MM-dd").parse("2019-6-25"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pa1.setPaperNote("Seksendört");
        paperRepository.save(pa1);*/


        alreadySetup = true;
    }

    private User createUserIfNotFound(String name , String role) {


        User user = userRepository.findByUsername(name);
        if (user == null) {
            //Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            Role adminRole = roleRepository.findByName(role);
            User u = new User();
            u.setFirstName("Celalettin");
            u.setLastName("Demir");
            u.setPassword(passwordEncoder.encode("1"));
            u.setUsername(name);
            u.setRoles(Arrays.asList(adminRole));
            u.setEnabled(true);
            userRepository.save(u);
        }
        return user;
    }

    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private void createLoggingTypesIfNotFound(String [] loggingTypes) {

        int length = loggingTypes.length;
        for(long i = 0 ; i< length;i++){
            LoggingEventType loggingEventType = loggingEventTypeRepository.findByEventType(loggingTypes[(int)i]);
            if (loggingEventType==null){
                LoggingEventType l1 = new LoggingEventType();
                l1.setEventType(loggingTypes[(int)i]);

                loggingEventTypeRepository.save(l1);

            }



        }



    }

    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}


