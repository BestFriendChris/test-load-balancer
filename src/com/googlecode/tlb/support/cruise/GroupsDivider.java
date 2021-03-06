package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.domain.Group;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GroupsDivider {

    static String hex = "[a-fA-F0-9]";
    static String uuid = hex + "{8}-" + hex + "{4}-" + hex + "{4}-" + hex + "{4}-" + hex + "{12}";
    static String number = "\\d+";

    private static final String NON_FIRST_JOB_NAME_PATTERN = "^(.*?)-(" + number + "|" + uuid + ")$";
    private static final Pattern PATTERN = Pattern.compile(NON_FIRST_JOB_NAME_PATTERN);

    public static Group divid(List<String> jobs, String thisJob) {
        String prefix = getPrefix(thisJob);
        List<String> thisGroup = new ArrayList<String>();
        for (String job : jobs) {
            if (job.equals(prefix) || job.startsWith(prefix + '-')) {
                thisGroup.add(job);
            }
        }
        String[] jobArray = thisGroup.toArray(new String[thisGroup.size()]);
        return new Group(jobArray, thisJob);
    }

    private static String getPrefix(String jobName) {
        Matcher matcher = PATTERN.matcher(jobName);
        return matcher.matches() ? matcher.group(1) : jobName;
    }
}
