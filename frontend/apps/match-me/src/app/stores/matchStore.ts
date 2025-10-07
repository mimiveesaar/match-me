"use client";
import { create } from "zustand";
import { Filters } from "@/app/matches/types"; // adjust the path if needed

interface FiltersState {
    filters: Filters;
    setFilters: (updater: Partial<Filters>) => void;
    resetFilters: () => void;
    initFromUser: (user: { homeplanet?: string }) => void;
}

export const useFiltersStore = create<FiltersState>((set) => ({
    filters: {
        minAge: 18,
        maxAge: 150,
        maxDistanceLy: 340,
        bodyform: "",
        interests: [],
        lookingFor: "",
        homeplanet: "",
    },
    setFilters: (updater) =>
        set((state) => ({
            filters: { ...state.filters, ...updater },
        })),
    resetFilters: () => //why the reset?
        set({
            filters: {
                minAge: 18,
                maxAge: 150,
                maxDistanceLy: 340,
                bodyform: "",
                interests: [],
                lookingFor: "",
                homeplanet: "",
            },
        }),

    initFromUser: (user) =>
        set((state) => ({
            filters: {
                ...state.filters,
                homeplanet: state.filters.homeplanet || user.homeplanet || "",
            },
        })),
}));
