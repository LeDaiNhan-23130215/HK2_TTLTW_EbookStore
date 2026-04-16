FROM tomcat:9.0-jdk11-openjdk
# Xóa app mặc định để web của bạn thành trang chủ
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy file war vừa build ở bước 1 vào (đổi tên thành ROOT.war)
COPY build/libs/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080