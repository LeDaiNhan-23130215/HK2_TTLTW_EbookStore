package mappers;

import models.Banner;

public class BannerCSVMapper implements CSVMapper<Banner>{
    @Override
    public Banner map(String[] cols) {
        String url = cols[0].trim();
        String position = cols[1].trim();
        String startDate = cols[2].trim();
        String endDate = cols[3].trim();
        int activeStr = Integer.parseInt(cols[4].trim());

        if (url.isEmpty() || position.isEmpty()
                || startDate.isEmpty() || endDate.isEmpty()) {
            return null;
        }

        return new Banner(url, position, startDate, endDate, activeStr);
    }
}
