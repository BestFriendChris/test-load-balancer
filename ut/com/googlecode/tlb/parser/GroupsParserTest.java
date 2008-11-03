package com.googlecode.tlb.parser;

import java.io.ByteArrayInputStream;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import com.googlecode.tlb.Groups;
import com.googlecode.tlb.Group;
import com.googlecode.tlb.support.twist.Group;

public class GroupsParserTest {

    @Test
    public void shouldParseGroupWithNoJob() throws Exception {
        String groupsInput = "[]";
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(groupsInput.getBytes("UTF-8")));
        GroupLexer lexer = new GroupLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroupParser parser = new GroupParser(tokens);
        Groups groups = parser.groups();
        assertThat(groups.size(), is(0));
    }

    @Test
    public void shouldParseEmptyGroup() throws Exception {
        String groupsInput = "";
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(groupsInput.getBytes("UTF-8")));
        GroupLexer lexer = new GroupLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroupParser parser = new GroupParser(tokens);
        Groups groups = parser.groups();
        assertThat(groups.size(), is(0));
    }


    @Test
    public void shouldParseOneGroupWithSingleJob() throws Exception {
        String groupsInput = "[job1 ]";
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(groupsInput.getBytes("UTF-8")));
        GroupLexer lexer = new GroupLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroupParser parser = new GroupParser(tokens);
        assertThat(parser.groups().get(0).contains("job1"), is(true));
    }

    @Test
    public void shouldParseOneGroupWithMultipleJobs() throws Exception {
        String groupsInput = "[job1, job2]";
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(groupsInput.getBytes("UTF-8")));
        GroupLexer lexer = new GroupLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroupParser parser = new GroupParser(tokens);
        Groups groups = parser.groups();
        Group group = groups.get(0);
        assertThat(groups.size(), is(1));
        assertThat(group.jobsCount(), is(2));
        assertThat(group.contains("job1"), is(true));
        assertThat(group.contains("job2"), is(true));
    }

    @Test
    public void shouldParseMultipleGroupsWithMultipleJobs() throws Exception {
        String groupsInput = "[job1, job2] [job3, job4, job5]";
        ANTLRInputStream input = new ANTLRInputStream(new ByteArrayInputStream(groupsInput.getBytes("UTF-8")));
        GroupLexer lexer = new GroupLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        GroupParser parser = new GroupParser(tokens);
        Groups groups = parser.groups();
        assertThat(groups.size(), is(2));
        Group group = groups.get(0);
        assertThat(group.jobsCount(), is(2));
        assertThat(group.contains("job1"), is(true));
        assertThat(group.contains("job2"), is(true));
        Group group2 = groups.get(1);
        assertThat(group2.jobsCount(), is(3));
        assertThat(group2.contains("job3"), is(true));
        assertThat(group2.contains("job4"), is(true));
        assertThat(group2.contains("job5"), is(true));
    }
}
