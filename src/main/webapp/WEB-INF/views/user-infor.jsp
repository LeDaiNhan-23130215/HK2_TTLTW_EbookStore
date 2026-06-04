<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Thông tin tài khoản</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/components.css"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/user-infor.css"/>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
  <link rel="icon" type="image/png" href="${pageContext.request.contextPath}/assets/img/ebook-logo2.png"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="container">

  <%-- ===== SIDEBAR TRÁI ===== --%>
  <div class="box-left">
    <div class="subTitle">
      <h5>TRANG TÀI KHOẢN</h5>
      <p><b>Xin chào, </b>
        <b style="color:hsl(0,100%,60%);font-weight:550">${sessionScope.userName}</b> !
      </p>
    </div>
    <ul class="option-list">
      <li class="selected"><a href="${pageContext.request.contextPath}/userInformation">Thông tin tài khoản</a></li>
      <li><a href="${pageContext.request.contextPath}/your-order">Đơn hàng của bạn</a></li>
      <li><a href="${pageContext.request.contextPath}/book-shelf">Tủ sách của bạn</a></li>
      <li><a href="${pageContext.request.contextPath}/wishlist">Danh mục yêu thích</a></li>
      <li><a href="${pageContext.request.contextPath}/change-password">Đổi mật khẩu</a></li>
    </ul>
  </div>

  <%-- ===== NỘI DUNG PHẢI ===== --%>
  <div class="box-right">
    <c:if test="${empty param.step}">
      <div class="subTitle"><h5>THÔNG TIN TÀI KHOẢN</h5></div>

      <form action="${pageContext.request.contextPath}/userInformation"
            method="post" id="uiForm">

        <div class="ui-info-row">
          <div class="ui-info-label">
            <i class="fa-solid fa-user"></i> Tên người dùng
          </div>
          <div class="ui-info-value">${sessionScope.userName}</div>
          <button type="button" class="ui-edit-btn"
                  onclick="uiToggleEdit('formUsername')">
            <i class="fa-solid fa-pen"></i> Sửa
          </button>
        </div>
        <div class="ui-edit-form" id="formUsername" style="display:none;">
          <div class="ui-edit-field">
            <input type="text" name="newUsername" id="ueNewUsername"
                   placeholder="Tên người dùng mới"
                   value="${sessionScope.userName}"/>
            <button type="submit" name="action" value="updateUsername"
                    class="ui-save-btn">Lưu</button>
            <button type="button" class="ui-cancel-btn"
                    onclick="uiToggleEdit('formUsername')">Huỷ</button>
          </div>
          <span id="ue-username-err" style="color:#c53030;font-size:12px;"></span>
        </div>

        <div class="ui-info-row">
          <div class="ui-info-label">
            <i class="fa-solid fa-envelope"></i> Email
          </div>
          <div class="ui-info-value">
              ${sessionScope.email}
            <c:if test="${not empty sessionScope.user.provider}">
              <span class="ui-oauth-badge">
                <i class="fa-brands fa-google"></i> Google
              </span>
            </c:if>
          </div>
          <c:set var="isLinkedGoogle"
                 value="${not empty sessionScope.user.provider}"/>
          <c:set var="isOAuthOnly"
                 value="${not empty sessionScope.user.provider
                 and (empty sessionScope.user.password
                 or sessionScope.user.password == null)}"/>
          <c:if test="${!isLinkedGoogle}">
            <a href="${pageContext.request.contextPath}/userInformation?step=change-email"
               class="ui-edit-btn">
              <i class="fa-solid fa-pen"></i> Sửa
            </a>
          </c:if>
        </div>

        <div class="ui-info-row">
          <div class="ui-info-label">
            <i class="fa-solid fa-phone"></i> Số điện thoại
          </div>
          <div class="ui-info-value">
            <c:choose>
              <c:when test="${not empty sessionScope.phoneNum}">
                ${sessionScope.phoneNum}
              </c:when>
              <c:otherwise>
                <span style="color:#aaa;">Chưa cập nhật</span>
              </c:otherwise>
            </c:choose>
          </div>
          <button type="button" class="ui-edit-btn"
                  onclick="uiToggleEdit('formPhone')">
            <i class="fa-solid fa-pen"></i> Sửa
          </button>
        </div>
        <div class="ui-edit-form" id="formPhone" style="display:none;">
          <div class="ui-edit-field">
            <input type="tel" name="newPhone" id="ueNewPhone"
                   placeholder="Số điện thoại mới"
                   value="${sessionScope.phoneNum}"/>
            <button type="submit" name="action" value="updatePhone"
                    class="ui-save-btn">Lưu</button>
            <button type="button" class="ui-cancel-btn"
                    onclick="uiToggleEdit('formPhone')">Huỷ</button>
          </div>
          <span id="ue-phone-err" style="color:#c53030;font-size:12px;"></span>
        </div>

        <c:if test="${isLinkedGoogle}">
          <div class="ui-info-row">
            <div class="ui-info-label">
              <i class="fa-brands fa-google"></i> Liên kết Google
            </div>
            <div class="ui-info-value">
              <span class="ui-oauth-badge">
                <i class="fa-brands fa-google"></i>
                Đã liên kết
              </span>
            </div>
            <c:if test="${!isOAuthOnly}">
              <button type="button"
                      class="ui-unlink-btn"
                      onclick="uiShowUnlinkModal()">
                <i class="fa-solid fa-link-slash"></i> Huỷ liên kết
              </button>
            </c:if>
          </div>
        </c:if>

      </form>
    </c:if>

    <%-- Bước 1/3: Xác minh danh tính --%>
    <c:if test="${param.step == 'change-email'}">
      <div class="subTitle"><h5>ĐỔI ĐỊA CHỈ EMAIL</h5></div>

      <%-- Progress steps --%>
      <div class="ue-steps">
        <div class="ue-step active">
          <div class="ue-step-circle">1</div>
          <span>Xác minh danh tính</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">2</div>
          <span>Nhập email mới</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">3</div>
          <span>Xác thực OTP</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">4</div>
          <span>Xác nhận</span>
        </div>
      </div>

      <div class="cp-inner">
        <c:if test="${not empty param.error}">
          <p class="fp-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>
              <c:choose>
                <c:when test="${param.error == 'wrongPassword'}">Mật khẩu không đúng. Vui lòng thử lại.</c:when>
                <c:when test="${param.error == 'emptyPassword'}">Vui lòng nhập mật khẩu hiện tại.</c:when>
                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
              </c:choose>
            </span>
          </p>
        </c:if>

        <p class="cp-send-hint">
          Để bảo mật tài khoản, vui lòng xác minh bằng mật khẩu hiện tại trước khi thay đổi email.
        </p>

        <form action="${pageContext.request.contextPath}/userInformation"
              method="post">
          <div class="cp-inner" style="margin-top:0;">
            <div class="fp-pw-wrap">
              <input type="password" name="password" id="uePassword"
                     placeholder="Mật khẩu hiện tại" autocomplete="current-password"/>
              <i class="fa-regular fa-eye ue-eye" data-target="uePassword"></i>
            </div>
            <div class="ui-step-actions" style="margin-top:6px;">
              <button type="submit" name="action" value="verifyPassword"
                      class="cp-send-btn">
                <i class="fa-solid fa-shield-halved"></i> Xác minh & Tiếp tục
              </button>
              <a href="${pageContext.request.contextPath}/userInformation"
                 class="ui-cancel-btn">Huỷ</a>
            </div>
          </div>
        </form>
      </div>
    </c:if>

    <%-- Bước 2/3: Nhập email mới --%>
    <c:if test="${param.step == 'new-email'}">
      <div class="subTitle"><h5>ĐỔI ĐỊA CHỈ EMAIL</h5></div>

      <%-- Progress steps --%>
      <div class="ue-steps">
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Xác minh danh tính</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step active">
          <div class="ue-step-circle">2</div>
          <span>Nhập email mới</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">3</div>
          <span>Xác thực OTP</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">4</div>
          <span>Xác nhận</span>
        </div>
      </div>

      <div class="cp-inner">
        <c:if test="${not empty param.error}">
          <p class="fp-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>
              <c:choose>
                <c:when test="${param.error == 'emailTaken'}">Email này đã được sử dụng bởi tài khoản khác.</c:when>
                <c:when test="${param.error == 'invalidEmail'}">Email không đúng định dạng. Vui lòng kiểm tra lại.</c:when>
                <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
              </c:choose>
            </span>
          </p>
        </c:if>

        <p class="cp-send-hint">
          Nhập địa chỉ email mới bạn muốn sử dụng. Một mã xác thực OTP sẽ được gửi đến email này.
        </p>

        <form action="${pageContext.request.contextPath}/userInformation"
              method="post">
          <div class="cp-inner" style="margin-top:0;">
            <div class="fp-pw-wrap">
              <input type="email" name="newEmail" id="ueNewEmail"
                     placeholder="Địa chỉ email mới" autocomplete="email"/>
              <i class="fa-solid fa-envelope" style="position:absolute;right:14px;top:50%;transform:translateY(-50%);color:#0396c7;pointer-events:none;"></i>
            </div>
            <span id="ue-email-err" style="color:#c53030;font-size:12px;margin-top:-8px;"></span>
            <div class="ui-step-actions" style="margin-top:6px;">
              <button type="submit" name="action" value="submitNewEmail"
                      class="cp-send-btn">
                <i class="fa-solid fa-paper-plane"></i> Gửi mã xác thực
              </button>
              <a href="${pageContext.request.contextPath}/userInformation"
                 class="ui-cancel-btn">Huỷ</a>
            </div>
          </div>
        </form>
      </div>
    </c:if>

    <%-- Bước 3/3: Xác thực OTP --%>
    <c:if test="${param.step == 'verify-email'}">
      <div class="subTitle"><h5>ĐỔI ĐỊA CHỈ EMAIL</h5></div>

      <%-- Progress steps --%>
      <div class="ue-steps">
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Xác minh danh tính</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Nhập email mới</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step active">
          <div class="ue-step-circle">3</div>
          <span>Xác thực OTP</span>
        </div>
        <div class="ue-step-line"></div>
        <div class="ue-step">
          <div class="ue-step-circle">4</div>
          <span>Xác nhận</span>
        </div>
      </div>

      <div class="cp-inner" style="margin-top:0;">
          <%-- Lỗi OTP --%>
        <c:if test="${not empty param.error}">
          <p class="fp-error-box">
            <i class="fa-solid fa-circle-exclamation"></i>
            <span>
                  <c:choose>
                    <c:when test="${param.error == 'expiredCode'}">Mã OTP đã hết hạn. Vui lòng nhấn gửi lại.</c:when>
                    <c:when test="${param.error == 'invalidCode'}">Mã OTP không đúng. Vui lòng kiểm tra lại email.</c:when>
                    <c:when test="${param.error == 'alreadyUsed'}">Mã OTP này đã được sử dụng. Vui lòng gửi lại.</c:when>
                    <c:otherwise>Có lỗi xảy ra, vui lòng thử lại.</c:otherwise>
                  </c:choose>
              </span>
          </p>
        </c:if>

          <%-- Cooldown --%>
        <c:if test="${not empty param.resendCooldown}">
          <p class="fp-error-box">
            <i class="fa-solid fa-clock"></i>
            <span>Vui lòng chờ thêm <strong>${param.resendCooldown} giây</strong> trước khi gửi lại.</span>
          </p>
        </c:if>

          <%-- Gửi lại thành công --%>
        <c:if test="${param.resent == 'true'}">
          <p class="fp-success-box">
            <i class="fa-solid fa-circle-check"></i>
            <span>Đã gửi lại mã OTP mới. Hiệu lực 5 phút.</span>
          </p>
        </c:if>

        <p class="fp-sent-to">
          Mã OTP đã gửi đến: <strong>${sessionScope.uePendingEmail}</strong>
        </p>

        <form action="${pageContext.request.contextPath}/userInformation"
              method="post" style="display:flex;flex-direction:column;gap:14px;width:100%;">
          <input type="hidden" name="action" value="verifyEmailOtp"/>
          <div class="code-input">
            <input type="text" name="confirmCode" id="ueOtpInput"
                   placeholder="Nhập mã OTP 6 chữ số"
                   maxlength="6" autofocus autocomplete="one-time-code"/>
            <button type="submit" id="ueSubmitBtn" class="code-btn">
              Xác nhận
            </button>
          </div>

          <div class="fp-countdown" id="ueCountdownWrap"
               data-seconds="${not empty otpSecondsRemaining ? otpSecondsRemaining : 300}"
               data-email="${sessionScope.uePendingEmail}">
            Mã hết hạn sau: <span id="ueCountdown">05:00</span>
          </div>

          <div class="fp-resend">
            <button type="button" id="ueResendBtn"
                    class="fp-resend-btn" disabled
                    onclick="location.href='${pageContext.request.contextPath}/userInformation?step=verify-email&action=verifyEmailOtp&resend=true'">
              <i class="fa-solid fa-rotate-right"></i>
              Gửi lại mã OTP
              <span id="ueResendCooldown">(30s)</span>
            </button>
          </div>
        </form>

        <div class="fp-back-link">
          <a href="${pageContext.request.contextPath}/userInformation">← Quay lại</a>
        </div>
      </div>
    </c:if>

    <%-- Bước 4/4: Xác nhận thay đổi --%>
    <c:if test="${param.step == 'confirm-email'}">
      <div class="subTitle"><h5>ĐỔI ĐỊA CHỈ EMAIL</h5></div>

      <div class="ue-steps">
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Xác minh danh tính</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Nhập email mới</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step done">
          <div class="ue-step-circle"><i class="fa-solid fa-check"></i></div>
          <span>Xác thực OTP</span>
        </div>
        <div class="ue-step-line active"></div>
        <div class="ue-step active">
          <div class="ue-step-circle">4</div>
          <span>Xác nhận</span>
        </div>
      </div>

      <div class="cp-inner">
        <div class="ui-confirm-box">
          <p><i class="fa-solid fa-envelope-circle-check" style="color:#0396c7;margin-right:6px;"></i> Bạn sắp đổi email sang địa chỉ mới:</p>
          <p><strong style="font-size:1.05rem;color:#0c4a6e;">${sessionScope.uePendingEmail}</strong></p>
          <p style="color:#555;font-size:13px;">
            Sau khi xác nhận, email đăng nhập của bạn sẽ được cập nhật ngay lập tức.
            Bạn sẽ cần sử dụng email mới để đăng nhập lần sau.
          </p>
        </div>

        <form action="${pageContext.request.contextPath}/userInformation"
              method="post">
          <div class="ui-step-actions">
            <button type="submit" name="action" value="confirmEmail"
                    class="cp-send-btn">
              <i class="fa-solid fa-check-double"></i> Xác nhận đổi email
            </button>
            <a href="${pageContext.request.contextPath}/userInformation"
               class="ui-cancel-btn">Huỷ</a>
          </div>
        </form>
      </div>
    </c:if>

  </div>
</div>

<%-- ===== CUSTOM MODAL: Huỷ liên kết Google ===== --%>
<div id="uiUnlinkModal" class="ui-modal-overlay" style="display:none;" aria-modal="true" role="dialog">
  <div class="ui-modal-box">
    <div class="ui-modal-icon">
      <i class="fa-brands fa-google"></i>
    </div>
    <h3 class="ui-modal-title">Huỷ liên kết Google?</h3>
    <p class="ui-modal-desc">
      Sau khi huỷ liên kết, bạn sẽ không thể đăng nhập bằng tài khoản Google này nữa.<br/>
      Bạn vẫn có thể đăng nhập bằng mật khẩu đã thiết lập.
    </p>
    <div class="ui-modal-actions">
      <button type="button" class="ui-modal-cancel" onclick="uiHideUnlinkModal()">
        Quay lại
      </button>
      <form action="${pageContext.request.contextPath}/userInformation" method="post" style="margin:0;">
        <button type="submit" name="action" value="unlinkGoogle" class="ui-modal-confirm">
          <i class="fa-solid fa-link-slash"></i> Huỷ liên kết
        </button>
      </form>
    </div>
  </div>
</div>

<jsp:include page="/WEB-INF/views/footer.jsp"/>
<script src="${pageContext.request.contextPath}/assets/js/component.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/user-infor.js"></script>
</body>
</html>
