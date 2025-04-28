/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
      "./src/**/*.{js,ts,jsx,tsx}",
    "./public/index.html"
  ],
  theme: {
    colors: {
      transparent: "transparent",
      red: {
        500: "#ff082e",
        900: "#FF082E19",
      },
      orange: {
        500: "#ff8600",
        900: "#FF860019",
      },
      yellow: {
        500: "#fdf800",
        900: "#FDF80019",
      },
      green: {
        500: "#00df00",
        900: "#00DF0019",
      },
      purple: "#9917cc",
      pink: "#ff01ff",
      blue: {
        500: "#0040fe",
        900: "#0040FE19",
      },
      cyan: {
        500: "#00c5ff",
        900: "#00C5FF19",
      },

      gray: {
        50: "#F7F7F7",
        100: "#E1E1E1",
        200: "#CFCFCF",
        300: "#B1B1B1",
        500: "#7E7E7E",
        600: "#626262",
        700: "#515151",
        800: "#3B3B3B",
        900: "#222222",
      },
      white: "#ffffff",
      black: "#000000",
      darken: "rgba(0, 0, 0, 0.5)",
    },
    extend: {
      fontFamily: {
        brand: ["Poppins", "sans-serif"],
      },
      keyframes: {
        'fade-in-left': {
          '0%': {
            opacity: '0',
            transform: 'translateX(-10%)'
          },
          '100%': {
            opacity: '1',
            transform: 'translateX(0)'
          },
        }
      },
      animation: {
        'fade-in-left': 'fade-in-left 0.5s ease-out'
      }
    },
  },
  plugins: [],
}

