package mappers;

import models.News;

public class NewsCSVMapper implements CSVMapper<News>{
    @Override
    public News map(String[] cols) {
        String title = cols[0].trim();
        String content = cols[1].trim();
        String imgURL = cols[2].trim();
        String author = cols[3].trim();
        int status = Integer.parseInt(cols[4].trim());

        if(title.isEmpty() || content.isEmpty() || imgURL.isEmpty() || author.isEmpty()){
            return null;
        }
        return new News(title, content, imgURL, author, status);
    }
}
