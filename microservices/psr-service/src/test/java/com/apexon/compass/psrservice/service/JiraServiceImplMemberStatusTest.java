package com.apexon.compass.psrservice.service;

import com.apexon.compass.aggregation.vo.SprintMemberStatusByIdVo;
import com.apexon.compass.aggregation.vo.SprintMemberStatusVo;
import com.apexon.compass.psrservice.dao.SprintDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class JiraServiceImplMemberStatusTest {

    private static final String ID_TOKEN = "token";

    private final String ISC_PROJECT_ID = "5e7e07e1215e091c132c8f9b";

    private final Integer BOARD_ID = 179;

    @Mock
    private SprintDao sprintDao;

    @Mock
    private JiraService jiraServiceImpl;

    @Test
    void testGetMemberStatusResponse() {

        SprintMemberStatusVo memberStatusVo = new SprintMemberStatusVo();
        memberStatusVo.setSprintId(11);
        memberStatusVo.setSprintId(10003);

        sprintDao.getMemberStatus(11, BOARD_ID, ISC_PROJECT_ID);
        Mockito.when(sprintDao.getMemberStatus(11, BOARD_ID, ISC_PROJECT_ID)).thenReturn(memberStatusVo);
        jiraServiceImpl.getMemberStatus("5e7dbe36215e091c132c8f88", BOARD_ID);
        Mockito.when(jiraServiceImpl.getMemberStatus("5e7dbe36215e091c132c8f88", BOARD_ID))
            .thenReturn(JiraServiceImplUtils.getSprintMemberStatusDto());

        assertThat(sprintDao.getMemberStatus(11, BOARD_ID, ISC_PROJECT_ID), is(memberStatusVo));
    }

    @Test
    void testGetMemberStatusResponseByMemberId() {

        SprintMemberStatusByIdVo sprintMemberStatusByIdVo = new SprintMemberStatusByIdVo();
        sprintMemberStatusByIdVo.setSprintId(11);
        sprintMemberStatusByIdVo.setSprintId(10003);

        sprintDao.getMemberStatus(11, BOARD_ID, ISC_PROJECT_ID);
        Mockito.when(sprintDao.getMemberStatusByMemberId(11, "5d1df9bbe3f1b90d1765f307", BOARD_ID, ISC_PROJECT_ID))
            .thenReturn(sprintMemberStatusByIdVo);
        Mockito
            .when(jiraServiceImpl.getMemberStatusById("5e7dbe36215e091c132c8f88", "5d1df9bbe3f1b90d1765f307", BOARD_ID))
            .thenReturn(JiraServiceImplUtils.getSprintMemberStatusByIdDtoDto());
        assertThat(sprintDao.getMemberStatusByMemberId(11, "5d1df9bbe3f1b90d1765f307", BOARD_ID, ISC_PROJECT_ID),
                is(sprintMemberStatusByIdVo));
    }

}
