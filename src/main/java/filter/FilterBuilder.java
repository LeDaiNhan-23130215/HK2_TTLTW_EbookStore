package filter;

import DTO.EbookFilterView;

public class FilterBuilder {

    public static EbookQueryFilter buildBase(EbookFilterView filter) {
        EbookQueryFilter query = new ApplyBaseFilter();

        if(filter.getCategoryId() != null) {
            query = new CategoryFilter(query, filter.getCategoryId());
        }

        if(filter.getFormats() != null) {
            query = new FormatFilter(query, filter.getFormats());
        }

        if(filter.getKeywords() != null) {
            query = new KeywordFilter(query, filter.getKeywords());
        }

        if(filter.getFree() != null) {
            query = new FreeFilter(query, filter.getFree());
        }

        return query;
    }

    public static EbookQueryFilter buildForList(EbookFilterView filter) {
        EbookQueryFilter query = buildBase(filter);
        query = new SortDecorator(query, filter.getSortBy(), filter.getSortDir());

        return query;
    }

    public static EbookQueryFilter buildForCount(EbookFilterView filter) {
        return buildBase(filter);
    }

}
