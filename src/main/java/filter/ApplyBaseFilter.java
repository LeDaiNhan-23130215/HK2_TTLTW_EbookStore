package filter;

public class ApplyBaseFilter implements EbookQueryFilter{
    @Override
    public QueryBase apply(QueryBase queryBase) {
        return queryBase;
    }
}
