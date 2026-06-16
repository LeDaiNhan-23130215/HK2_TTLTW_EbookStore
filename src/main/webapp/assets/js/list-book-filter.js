document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("filterForm");
  const grid = document.getElementById("grid-container");
  const container = document.getElementById("active-filters");

  if (!form || !grid || !container) return;

  function fetchData() {
    const params = new URLSearchParams(new FormData(form));
    const keywordInput = form.querySelector('[name="keyword"]');
    let keyword = keywordInput ? keywordInput.value.trim() : "";

    if (!keyword) {
      const urlParams = new URLSearchParams(window.location.search);
      keyword = (urlParams.get("keyword") || "").trim();
    }

    if (keyword) {
      params.set("keyword", keyword);
      if (keywordInput) keywordInput.value = keyword;
    } else {
      params.delete("keyword");
    }

    params.set("page", "1");

    // ===== CẬP NHẬT URL KHÔNG RELOAD =====
    const newUrl = `${window.location.pathname}?${params.toString()}`;
    window.history.pushState({}, "", newUrl);

    // ===== GỌI AJAX =====
    fetch("list-book?" + params.toString(), {
      headers: { "X-Requested-With": "XMLHttpRequest" }
    })
        .then(res => res.text())
        .then(html => {
          grid.innerHTML = html;
          renderActiveFilters();
          setupPaginationLinks(); // Gắn lại event listeners cho pagination mới
          updateSortButtonsState(); // Cập nhật trạng thái active của sort buttons
        })
        .catch(err => {
          console.error("Error fetching data:", err);
        });
  }

  function getLabelForInput(input) {
    if (input.type === "checkbox" || input.type === "radio") {
      return input.parentElement.textContent.trim();
    }
    return input.value;
  }

  function renderActiveFilters() {
    container.innerHTML = "";

    const activeFilters = [];

    // ===== LẤY FILTER TỪ FORM =====
    [...form.elements].forEach(input => {
      if (!input.name || !input.value) return;
      if (input.name === "sortBy" || input.name === "sortDir") return;

      if ((input.type === "checkbox" || input.type === "radio") && input.checked) {
        activeFilters.push({ input, label: getLabelForInput(input) });
      }
    });

    // ===== LẤY SEARCH TỪ URL =====
    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get("keyword");

    if (keyword && keyword.trim() !== "") {
      activeFilters.push({
        input: { name: "keyword", type: "text", value: keyword },
        label: "🔍 " + keyword.trim()
      });
    }

    if (activeFilters.length === 0) {
      container.style.display = "none";
      return;
    }

    container.style.display = "flex";

    const title = document.createElement("p");
    title.className = "active-filter-title";
    title.textContent = "Đã lọc:";
    container.appendChild(title);

    activeFilters.forEach(({ input, label }) => {
      const tag = document.createElement("div");
      tag.className = "filter-tag";
      tag.innerHTML = `
        <span>${label}</span>
        <button type="button">&times;</button>
      `;

      tag.querySelector("button").addEventListener("click", () => {
        // ===== XÓA SEARCH =====
        if (input.name === "keyword") {
          const urlParams = new URLSearchParams(window.location.search);
          urlParams.delete("keyword");

          // Reload trang với URL mới (không có keyword)
          window.location.search = urlParams.toString();
          return;
        }

        // ===== XÓA FILTER =====
        if (input.type === "checkbox" || input.type === "radio") {
          [...form.querySelectorAll(`[name="${input.name}"]`)].forEach(i => {
            if (i.value === input.value) i.checked = false;
          });
        }

        if (input.name === "free") {
          form.querySelector(`[name="free"][value=""]`).checked = true;
        }

        fetchData();
      });

      container.appendChild(tag);
    });
  }

  function updateSortButtonsState() {
    const sortByInput = form.querySelector('[name="sortBy"]');
    const sortDirInput = form.querySelector('[name="sortDir"]');

    const currentSortBy = sortByInput ? sortByInput.value : 'created_at';
    const currentSortDir = sortDirInput ? sortDirInput.value : 'desc';

    // Xóa class active khỏi tất cả sort buttons
    document.querySelectorAll('.sort-button').forEach(btn => {
      btn.classList.remove('active');
    });

    // Thêm class active vào button tương ứng
    document.querySelectorAll('.sort-button').forEach(btn => {
      const url = new URL(btn.href);
      const btnSortBy = url.searchParams.get('sortBy');
      const btnSortDir = url.searchParams.get('sortDir');

      if (btnSortBy === currentSortBy && btnSortDir === currentSortDir) {
        btn.classList.add('active');
      }
    });
  }

  // ===== XỬ LÝ SORT BUTTONS =====
  function setupSortButtons() {
    document.querySelectorAll('.sort-button').forEach(button => {
      button.addEventListener('click', (e) => {
        e.preventDefault(); // Ngăn không cho reload trang
        e.stopPropagation(); // Ngăn event bubbling

        const url = new URL(button.href);
        const sortBy = url.searchParams.get('sortBy');
        const sortDir = url.searchParams.get('sortDir');

        // Cập nhật giá trị sort trong form
        const sortByInput = form.querySelector('[name="sortBy"]');
        const sortDirInput = form.querySelector('[name="sortDir"]');

        if (sortByInput) sortByInput.value = sortBy;
        if (sortDirInput) sortDirInput.value = sortDir;

        // Gọi fetchData để load lại dữ liệu
        fetchData();
      });
    });
  }

  function setupPaginationLinks() {
    document.querySelectorAll('.pagination a').forEach(link => {
      link.addEventListener('click', (e) => {
        e.preventDefault();
        e.stopPropagation();

        const url = new URL(link.href);
        const page = (url.searchParams.get('page') || '1').trim();
        const params = new URLSearchParams(new FormData(form));

        const urlParams = new URLSearchParams(window.location.search);
        const keyword = urlParams.get("keyword");
        if (keyword && keyword.trim() !== "") {
          params.set("keyword", keyword.trim());
        }

        params.set("page", page);

        const newUrl = `${window.location.pathname}?${params.toString()}`;
        window.history.pushState({}, "", newUrl);

        // Gọi AJAX
        fetch("list-book?" + params.toString(), {
          headers: { "X-Requested-With": "XMLHttpRequest" }
        })
            .then(res => res.text())
            .then(html => {
              grid.innerHTML = html;
              setupPaginationLinks(); // Gắn lại event cho pagination mới

              // Scroll lên đầu danh sách
              document.querySelector('.content').scrollIntoView({
                behavior: 'smooth',
                block: 'start'
              });
            })
            .catch(err => {
              console.error("Error loading page:", err);
            });
      });
    });
  }

  // ===== BẮT SỰ KIỆN FILTER =====
  form.querySelectorAll("input").forEach(input => {
    input.addEventListener("change", fetchData);
  });

  // ===== KHỞI TẠO =====
  renderActiveFilters();
  setupSortButtons();
  setupPaginationLinks(); // Gắn event cho pagination ban đầu
  updateSortButtonsState();

  // ===== XỬ LÝ BACK/FORWARD BUTTON =====
  window.addEventListener("popstate", () => {
    location.reload();
  });
});