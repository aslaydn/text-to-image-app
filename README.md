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

<h3>🏗️ Architecture<h3/>
```mermaid  
graph TD
    A[UI Layer] --> B[MainActivity]
    B --> C[Translation Service]
    B --> D[Network Layer]
    B --> E[Storage Service]
    C --> F[Google ML Kit]
    D --> G[HuggingFace API]
    E --> H[Device Gallery]

    style A fill:#e1f5fe
    style B fill:#e1f5fe
    style C fill:#fff3e0
    style D fill:#fff3e0
    style E fill:#fff3e0
    style F fill:#f5f5f5
    style G fill:#f5f5f5
    style H fill:#f5f5f5
    
