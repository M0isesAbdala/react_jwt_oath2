import AppContent from './context/AppContent'
import AuthProvider from './context/auth/AuthContext'
import ThemeContext from './context/theme/ThemeContext'

function App() {
  return (
    <AuthProvider>
      <ThemeContext>
        <AppContent />
      </ThemeContext>
    </AuthProvider>
  )
}

export default App