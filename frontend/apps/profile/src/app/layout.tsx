
import type { Metadata } from 'next'
import { IBM_Plex_Sans } from 'next/font/google'
import '/home/katikraavi/match-me/frontend/packages/components/public/style/styles.css'

const ibmPlexSans = IBM_Plex_Sans({ 
  weight: ['400', '600'],
  subsets: ['latin'],
  variable: '--font-ibm-plex-sans'
})

export const metadata: Metadata = {
  title: 'Profile App',
  description: 'Profile management application',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={ibmPlexSans.variable}>{children}</body>
    </html>
  )
}
