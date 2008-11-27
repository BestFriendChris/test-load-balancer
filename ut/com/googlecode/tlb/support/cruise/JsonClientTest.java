package com.googlecode.tlb.support.cruise;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import com.googlecode.tlb.support.cruise.JsonClient;

import java.util.List;

public class JsonClientTest {
    @Test
    public void shouldLoadAllJobsInSpecificStage() throws Exception {
        JsonClient jsonClient = new JsonClient(PIPELINESTATUSJSON, "evolve", "dev");
        List<String> strings = jsonClient.getJobsInStage();
        assertThat(strings.size(), is(3));
    }

    private static final String PIPELINESTATUSJSON =
            "{ \"pipelines\" : [ { \"name\" : \"evolve\", \"paused\" : \"false\", \"pauseCause\" : \"\", "
                    + "\"pauseBy\" : \"\", \"stages\""
                    + " : [ { \"pipelineId\" : \"22\", \"pipelineName\" : \"evolve\", \"uniqueStageId\" : "
                    + "\"evolve-dev-82\", \"buildCause\""
                    + " : \"modified by committer\", \"buildCauseSummaries\" : [ { \"username\" : \"committer\","
                    + " \"comment\" : \"Added"
                    + " the README file\", \"revision\" : \"100\", \"modified_date\" :"
                    + " \"2008-09-18 16:43:10 +0800\" }, { \"username\""
                    + " : \"lgao\", \"comment\" : \"Fixing the not checked in files\", "
                    + "\"revision\" : \"99\", \"modified_date\" : \"2008-09-17"
                    + " 16:43:10 +0800\" } ], \"stageName\" : \"dev\", \"current_label\":"
                    + " \"11\", \"id\" : \"82\", \"builds\" : [ { \"agent\""
                    + " : \"Not yet assigned\", \"build_scheduled_date\" "
                    + ": \"2008-09-18 16:38:13 +0800\", \"build_assigned_date\" :"
                    + " \"2008-09-18 16:39:13 +0800\", \"build_preparing_date\" : "
                    + "\"2008-09-18 16:40:13 +0800\", \"build_building_date\""
                    + " : \"2008-09-18 16:41:13 +0800\", \"build_completing_date\" : "
                    + "\"N/A\", \"build_completed_date\" : \"N/A\", \"current_status\""
                    + " : \"building\", \"current_build_duration\" : \"291\", \"last_build_duration\" "
                    + ": \"0\", \"id\" : \"405\", \"is_completed\""
                    + " : \"false\", \"name\" : \"buildPlan-1\", \"result\" : \"Unknown\" }, "
                    + "{ \"agent\" : \"Not yet assigned\", \"build_scheduled_date\""
                    + " : \"2008-09-18 16:43:13 +0800\", \"build_assigned_date\" : \"N/A\", "
                    + "\"build_preparing_date\" : \"N/A\", \"build_building_date\""
                    + " : \"N/A\", \"build_completing_date\" : \"N/A\", \"build_completed_date\" "
                    + ": \"N/A\", \"current_status\" : \"discontinued\""
                    + ", \"current_build_duration\" : \"0\", \"last_build_duration\" : \"0\", "
                    + "\"id\" : \"403\", \"is_completed\" : \"false\""
                    + ", \"name\" : \"buildPlan-2\", \"result\" : \"Unknown\" }, { \"agent\" : "
                    + "\"Not yet assigned\", \"build_scheduled_date\""
                    + " : \"2008-09-18 16:43:13 +0800\", \"build_assigned_date\" : "
                    + "\"N/A\", \"build_preparing_date\" : \"N/A\", \"build_building_date\""
                    + " : \"N/A\", \"build_completing_date\" : \"N/A\", \"build_completed_date\" : "
                    + "\"N/A\", \"current_status\" : \"scheduled\""
                    + ", \"current_build_duration\" : \"0\", \"last_build_duration\" : \"0\", \"id\" : \"404\","
                    + " \"is_completed\" : \"false\""
                    + ", \"name\" : \"unit\", \"result\" : \"Unknown\" } ], \"current_status\" : \"building\" } ],"
                    + " \"forcedBuild\" : \"false\""
                    + ", \"canForce\" : \"true\" } ] }";
}
