"use client";

import { MenuHeader } from "@atoms/Menu/Header/header";
import { MenuBase } from "@atoms/Menu/MenuBase/menu_base";
import { PageLink } from "@atoms/Menu/PageLink/pagelink";
import { SignOutButton } from "@atoms/Menu/SignOut/signout";
import React, { useState } from "react";

interface MenuProps {
  className?: string;
}

export const Menu = ({ className }: MenuProps) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => setIsOpen(!isOpen);

  return (
    <>
      {/* Hamburger Button - always visible */}
      <button
        onClick={toggleMenu}
        className="fixed top-4 left-4 z-50 p-2 bg-olive text-white rounded-md shadow-lg hover:bg-amberglow transition-colors lg:hidden"
        aria-label="Toggle menu"
      >
        <svg
          className="w-6 h-6"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
        >
          {isOpen ? (
            // X icon when menu is open
            <>
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </>
          ) : (
            // Hamburger icon when menu is closed
            <>
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
            </>
          )}
        </svg>
      </button>

      {/* Overlay for mobile when menu is open */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-30 lg:hidden"
          onClick={toggleMenu}
        />
      )}

      {/* Menu */}
      <div
        className={`
          fixed lg:static top-0 left-0 h-full w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-40
          ${isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'}
          ${className || ''}
        `}
      >
        <MenuBase className="flex flex-col h-full p-4">
          {/* Top: Header with close button for mobile */}
          <div className="w-full flex justify-between items-center mb-10">
            <MenuHeader header="Menu" />
            <button
              onClick={toggleMenu}
              className="lg:hidden p-1 text-gray-500 hover:text-gray-700"
              aria-label="Close menu"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          {/* Middle: PageLinks */}
          <div className="flex flex-col gap-3 items-start pl-2">
            <PageLink label="Matches" href="/matches" onClick={() => setIsOpen(false)} />
            <PageLink label="My Profile" href="/MyProfilePage" onClick={() => setIsOpen(false)} />
            <PageLink label="My Connections" href="/connections" onClick={() => setIsOpen(false)} />
            <PageLink label="Chatspace" href="/chatspace" onClick={() => setIsOpen(false)} />
          </div>

          {/* Spacer pushes sign-out to bottom */}
          <div className="flex-1" />

          {/* Bottom: Sign out centered */}
          <div className="w-full flex justify-center mt-4">
            <SignOutButton />
          </div>
        </MenuBase>
      </div>
    </>
  );
};