<h1> 🎨 AI Text-to-Image Generator </h1> 
<div align="center">
  <br/>
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="banner image" />
  <img src="https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white" alt="banner image" />
  <img src="https://img.shields.io/badge/huggingface-FFD21E.svg?style=for-the-badge" alt="banner image" />
  <img src="https://img.shields.io/badge/ML_Kit-4285F4?style=for-the-badge&logo=google&logoColor=white" alt="banner image" />
  <br/>
<h4>Generate stunning AI images from text descriptions with native language support</h4>
</div>
  
<h2>✨ Features<h2/>
  
<h3>🌍 Multilingual Support<h3/>
<ul>
  <li>Input text in Turkish (native language)</li>
  <li>On-device translation using ML Kit</li>
  <li>Offline translation capability</li>
</ul>


<h3>🎯 Core Features<h3/>
<ul>
  <li>AI-powered image generation</li>
  <li>Automatic gallery saving</li>
  <li>User-friendly interface</li>
  <li>Real-time image preview</li>
  <li>Adaptive storage handling</li>
  <li>Comprehensive error management</li>
</ul>
<br/>
<h3>🏗️ Architecture<h3/>
<img width="735" alt="image" src="https://github.com/user-attachments/assets/c70a16af-143b-4eba-9b64-2f8dd99e7695" />

<br/>
<h3>💫 User Flow<h3/>
<img width="707" alt="image" src="https://github.com/user-attachments/assets/ec350c75-f91f-4535-9d44-2f9cb57f01cd" />
<br/>

<h3>🛠️ Tech Stack<h3/>
<h3>Core Technologies<h3/>
<ul>
  <li>🎯 Kotlin: Primary programming language</li>
  <li>🌐 ML Kit: Translation service</li>
  <li>🖼️ HuggingFace: Image generation API</li>
  <li>🔄 Coroutines: Asynchronous operations</li>
  <li>📱 Material Design: UI components</li>
  <li>💾 MediaStore: Storage management</li>
</ul>

<h3>Architecture Components<h3/>
<ul>
  <li>ViewBinding for UI interactions</li>
  <li>Retrofit for network operations</li>
  <li>Adaptive storage system for different Android version</li>
  <li>Comprehensive error handling system</li>
</ul>

<h3>📱 Screenshots<h3/>
<div align="center">
  <img width="200"height="410" alt="image" src="https://github.com/user-attachments/assets/2dfeb0f4-c637-45a4-99bd-8465e31780a3" />
  <img width="200" height="410" alt="image" src="https://github.com/user-attachments/assets/7eb7004c-5575-4cd6-bd11-e464aa055e3c" />
  <img width="200" height="410" alt="image" src="https://github.com/user-attachments/assets/c66964f0-206a-4a7d-ac83-0f231e2f1707" />
</div>

<h3>⚙️ Setup & Installation<h3/>
<ol>
  <li>Clone the repository: 
    <pre><code>git clone https://github.com/aslaydn/text-to-image-app.git</code></pre>
  </li>
  <li>Add your API key in <code>local.properties:</code>
    <pre><code>HUGGING_FACE_API_KEY=your_api_key_here</code></pre>
  </li>
    <li>Required dependencies:
    <pre><code>dependencies {
    // ML Kit Translation
    implementation 'com.google.mlkit:translate:17.0.1'
    
    // Retrofit for API calls
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    
    // Material Design
    implementation 'com.google.android.material:material:1.9.0'
}</code></pre>
  </li>
</ol>

<h3>🚀 Getting Started<h3/>
<ol>
  <li>Input text in Turkish</li>
  <li>Click "Generate Image" button</li>
  <li>Wait for the translation and image generation</li>
  <li>View the generated image</li>
  <li>Save to gallery if desired</li>
</ol>

<h3>🤝 Contributing<h3/>
<ol>
  <li>Fork the repository</li>
  <li>Create your feature branch <code>(git checkout -b feature/amazing-feature)</code></li>
  <li>Commit your changes <code>(git commit -m 'feat: Add some amazing feature')</code></li>
  <li>Push to the branch <code>(git push origin feature/amazing-feature)View the generated image</code></li>
  <li>Open a Pull Request</li>
</ol>

<h3>🙏 Acknowledgments<h3/>
<ul>
  <li><a href="https://huggingface.co/ " style="color: inherit; text-decoration: none;">HuggingFace for AI model</a>

</li>
  <li><a href="https://developers.google.com/ml-kit" style="color: inherit; text-decoration: none;">Google ML Kit for translation services</a>
</li>
  <li><a href="https://material.io/design" style="color: inherit; text-decoration: none;">Material Design for UI components</a>
</li>
</ul>
