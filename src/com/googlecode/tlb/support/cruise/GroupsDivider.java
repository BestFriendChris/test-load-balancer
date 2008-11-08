package com.googlecode.tlb.support.cruise;

import com.googlecode.tlb.support.twist.Group;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class GroupsDivider {

    public static Group divid(List<String> jobs, String thisJob) {
        String prefix = getPrefix(thisJob);
        List<String> jobsInOneGroup = new ArrayList<String>();
        for (String job : jobs) {
            if (job.equals(prefix) || job.contains(prefix + '-')) {
                jobsInOneGroup.add(job);
            }
        }
        return new Group(jobsInOneGroup.toArray(new String[]{}));
    }

    private static String getPrefix(String thisJob) {
        String endsWithDigit = ".*-(\\d)+";
        Pattern pattern = Pattern.compile(endsWithDigit);
        Matcher matcher = pattern.matcher(thisJob);
        if (matcher.matches()) {
            String digit = matcher.group(1);
            int index = thisJob.lastIndexOf("-" + digit);
            return thisJob.substring(0, index);
        } else {
            return thisJob;
        }
    }
}
