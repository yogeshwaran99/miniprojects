const postsContainer = document.getElementById("posts");


const defaultPosts = [
  {
    title: "Welcome to My Blog",
    content: "This is a simple blog made with HTML, CSS, and JavaScript only!",
    date: "2024-01-01 10:00 AM",
    tags: ["intro", "html", "blog"]
  },
  {
    title: "Frontend Development",
    content: "HTML, CSS, and JavaScript are the building blocks of the web.",
    date: "2024-02-10 3:45 PM",
    tags: ["frontend", "css", "js"]
  },
  {
    title: "Why Learn JavaScript?",
    content: "JavaScript lets you build interactive and dynamic websites.",
    date: "2024-03-15 9:30 AM",
    tags: ["javascript", "dynamic", "web"]
  }
];


async function getUserPosts() {
  try {
    const res = await fetch("http://localhost:8081/Server/BlogServlet");
    if (!res.ok) throw new Error("Failed to fetch blogs");
    const blogs = await res.json();
    return blogs;
  } catch (err) {
    console.error("Error fetching posts:", err);
    return [];
  }
}

function saveUserPosts(posts) {
  localStorage.setItem("userPosts", JSON.stringify(posts));
}

async function renderPosts() {
  const userPosts = await getUserPosts();
  const allPosts = [...defaultPosts, ...userPosts];
  postsContainer.innerHTML = "";

  allPosts.forEach((post, index) => {
    const postEl = document.createElement("article");
    postEl.classList.add("post");

    postEl.innerHTML = `
      ${post.image ? `<img src="${post.image}" class="recent-image" alt="Post image" />` : ""}
      <time class="recent-date">${new Date(post.date).toDateString()}</time>
      <h3 class="recent-title">
        <a href="#" onclick="viewFullPost(${post.id})">${post.title}</a>
      </h3>
      <p>${post.content.length > 100 ? post.content.slice(0, 100) + "..." : post.content}</p>
 
    ${post.tags
        ? `<div class="recent-tags">${(Array.isArray(post.tags) ? post.tags : post.tags.split(","))
          .map(tag => `<span class="recent-tag">${tag.trim()}</span>`)
          .join("")
        }</div>`
        : ""}


    `;

    postsContainer.appendChild(postEl);
  });
}


const toggle = document.getElementById("darkToggle");
if (toggle) {
  if (localStorage.getItem("darkMode") === "true") {
    document.body.classList.add("dark");
  }

  toggle.addEventListener("click", () => {
    document.body.classList.toggle("dark");
    const isDark = document.body.classList.contains("dark");
    localStorage.setItem("darkMode", isDark);
    toggle.textContent = isDark ? "☀️" : "🌙";
  });

  toggle.textContent = document.body.classList.contains("dark") ? "☀️" : "🌙";
}

renderPosts();

async function viewFullPost(id) {
  const userPosts = await getUserPosts();
  const post = userPosts.find(i => i.id === id);
  localStorage.setItem("selectedPost", JSON.stringify(post));
  window.location.href = "post.html";
}
