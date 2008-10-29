package com.thoughtworks.twist.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

import com.thoughtworks.twist.core.parser.TagLineParser;

public class Tags {

    private final List<Tag> tags;

    public Tags(Scenario scenario) {
        tags = scenario.collectLines(Tag.class);
    }

    public Tags(List<Tag> tags) {
        this.tags = tags;
    }

    public Tags() {
        this.tags = new ArrayList<Tag>();
    }

    public boolean intersects(Tags other) {
        List<Tag> tagsNotNeeded = new ArrayList<Tag>(other.tags);
        CollectionUtils.filter(tagsNotNeeded, new NotTagPredicate());

        List<Tag> tagsNeeded = new ArrayList<Tag>(other.tags);
        tagsNeeded.removeAll(tagsNotNeeded);
        CollectionUtils.transform(tagsNotNeeded, new NotTagTransformer());

        tagsNeeded.retainAll(tags);
        boolean intersects = !tagsNeeded.isEmpty();
        return intersects && !CollectionUtils.containsAny(tags, tagsNotNeeded);

    }

    public boolean hasTags(Tags other) {
        List<Tag> tagsNotNeeded = new ArrayList<Tag>(other.tags);
        CollectionUtils.filter(tagsNotNeeded, new NotTagPredicate());

        List<Tag> tagsNeeded = new ArrayList<Tag>(other.tags);
        tagsNeeded.removeAll(tagsNotNeeded);

        CollectionUtils.transform(tagsNotNeeded, new NotTagTransformer());

        return tags.containsAll(tagsNeeded) && !CollectionUtils.containsAny(tags, tagsNotNeeded);
    }

    public boolean similarTo(Tags other) {
        List tagFilter = new ArrayList(other.tags);
        CollectionUtils.transform(tagFilter, new TagTransformer());

        List tags = new ArrayList(this.tags);
        CollectionUtils.transform(tags, new TagTransformer());
        return tags.containsAll(tagFilter);
    }

    public void removeAllTagsBeginningWith(final Tag beginningWith) {
        CollectionUtils.filter(tags, new Predicate() {

            public boolean evaluate(Object object) {
                return ((Tag) object).matches(beginningWith);
            }
        });
    }

    public Tags removeAll(Tags tags) {
        this.tags.removeAll(tags.tags);
        return this;
    }

    public static Tags toTags(String tags) {
        Fragment parse = new TagLineParser().parse(TagElement.TAGS_PREFIX + tags.trim());
        return new Tags(parse.collect(Tag.class));
    }

    private static class TagTransformer implements Transformer {

        public Object transform(Object input) {
            return new TagStartsWithMatcher((Tag) input);
        }
    }

    private static class NotTagTransformer implements Transformer {

        public Object transform(Object input) {
            String tagText = ((Tag) input).tagText();
            return tagText.startsWith("!") ? new Tag(tagText.substring(1)) : input;
        }
    }

    private static class NotTagPredicate implements Predicate {

        public boolean evaluate(Object object) {
            return ((Tag) object).tagText().startsWith("!");
        }

    }

    public boolean isEmpty() {
        return tags.isEmpty();
    }
}
